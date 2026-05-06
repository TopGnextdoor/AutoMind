package com.automind.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.automind.data.local.AutoMindDatabase
import com.automind.data.local.AutopilotPreferences
import com.automind.data.model.AutopilotLogEntity
import com.automind.data.model.RuleEntity
import com.automind.data.model.SuggestionEntity
import com.automind.data.model.SuggestionStatus
import com.automind.data.model.UsageHistoryEntity
import com.automind.data.repository.RuleRepository
import com.automind.logic.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AutoMindViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RuleRepository
    private val database = AutoMindDatabase.getDatabase(application)
    private val historyDao = database.usageHistoryDao()
    private val suggestionDao = database.suggestionDao()
    private val autopilotDao = database.autopilotDao()
    private val autopilotPrefs = AutopilotPreferences(application)
    private val usageTracker: UsageTracker
    private val analyticsEngine = AnalyticsEngine()
    private val suggestionEngine = SuggestionEngine(HabitLearner(), suggestionDao)
    private val permissionManager = PermissionManager(application)
    
    val allRules: StateFlow<List<RuleEntity>>
    val dangerZones: StateFlow<List<Int>>
    val pendingSuggestions: StateFlow<List<SuggestionEntity>>
    val usageByHour: StateFlow<Map<Int, Long>>
    val topDistractions: StateFlow<List<Pair<String, Long>>>
    
    val isPermissionsGranted = MutableStateFlow(permissionManager.isAllGranted())
    
    val isAutopilotEnabled = autopilotPrefs.isAutopilotEnabled
    val disciplineScore = autopilotPrefs.disciplineScore
    val autopilotLogs = autopilotDao.getRecentLogs()
    
    private val _todayUsage = MutableLiveData<Map<String, Long>>()
    val todayUsage: LiveData<Map<String, Long>> = _todayUsage

    init {
        val database = AutoMindDatabase.getDatabase(application)
        repository = RuleRepository(database.ruleDao())
        usageTracker = UsageTracker(application)
        
        allRules = repository.allRules.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        dangerZones = historyDao.getRecentHistory().map { history ->
            PatternAnalyzer().detectDangerZones(history)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        pendingSuggestions = suggestionDao.getPendingSuggestions().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        val historyFlow = historyDao.getRecentHistory()
        
        usageByHour = historyFlow.map { 
            analyticsEngine.getUsageByHour(it)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

        topDistractions = historyFlow.map { 
            analyticsEngine.getTopDistractions(it)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
        refreshUsage()
        analyzeHabits()
    }

    private fun analyzeHabits() = viewModelScope.launch {
        historyDao.getAllHistory().let { history ->
            suggestionEngine.analyzeAndGenerateSuggestions(history)
        }
    }

    fun acceptSuggestion(suggestion: SuggestionEntity) = viewModelScope.launch {
        // 1. Mark as accepted
        suggestionDao.updateSuggestion(suggestion.copy(status = SuggestionStatus.ACCEPTED))
        
        // 2. Create corresponding rule
        val startHour = suggestion.hourOfDay
        val endHour = (startHour + 1) % 24
        repository.insert(RuleEntity(
            title = "Habit: ${suggestion.packageName.split(".").last()}",
            description = "Auto-suggested focus rule",
            targetPackage = suggestion.packageName,
            startTime = String.format("%02d:00", startHour),
            endTime = String.format("%02d:00", endHour)
        ))
    }

    fun rejectSuggestion(suggestion: SuggestionEntity) = viewModelScope.launch {
        suggestionDao.updateSuggestion(suggestion.copy(status = SuggestionStatus.REJECTED))
    }

    fun toggleAutopilot(enabled: Boolean) = viewModelScope.launch {
        autopilotPrefs.setAutopilotEnabled(enabled)
    }

    fun checkPermissions() {
        isPermissionsGranted.value = permissionManager.isAllGranted()
        if (isPermissionsGranted.value) {
            refreshUsage()
        }
    }

    fun refreshUsage() {
        viewModelScope.launch {
            try {
                _todayUsage.postValue(usageTracker.getTodayUsage())
            } catch (e: Exception) {
                // Log or handle error
            }
        }
    }

    fun addRule(rule: RuleEntity) = viewModelScope.launch {
        repository.insert(rule)
    }

    fun updateRule(rule: RuleEntity) = viewModelScope.launch {
        repository.update(rule)
    }

    fun deleteRule(rule: RuleEntity) = viewModelScope.launch {
        repository.delete(rule)
    }
}
