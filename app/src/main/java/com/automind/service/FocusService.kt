import android.app.*
import android.content.*
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.automind.MainActivity
import com.automind.data.local.AutoMindDatabase
import com.automind.data.local.AutopilotDao
import com.automind.data.local.AutopilotPreferences
import com.automind.data.local.UsageHistoryDao
import com.automind.data.model.AutopilotLogEntity
import com.automind.data.model.UsageHistoryEntity
import com.automind.data.repository.RuleRepository
import com.automind.logic.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.Calendar

class FocusService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var repository: RuleRepository
    private lateinit var historyDao: UsageHistoryDao
    private lateinit var autopilotDao: AutopilotDao
    private lateinit var autopilotPrefs: AutopilotPreferences
    private lateinit var usageTracker: UsageTracker
    private lateinit var ruleEngine: RuleEngine
    private lateinit var contextEngine: ContextEngine
    private lateinit var autopilotManager: AutopilotManager
    private var job: Job? = null

    private val screenStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF -> stopMonitoring()
                Intent.ACTION_SCREEN_ON -> startMonitoring()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val database = AutoMindDatabase.getDatabase(this)
        repository = RuleRepository(database.ruleDao())
        historyDao = database.usageHistoryDao()
        autopilotDao = database.autopilotDao()
        autopilotPrefs = AutopilotPreferences(this)
        
        usageTracker = UsageTracker(this)
        ruleEngine = RuleEngine()
        val analyzer = PatternAnalyzer()
        contextEngine = ContextEngine(analyzer)
        autopilotManager = AutopilotManager(analyzer)
        
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenStateReceiver, filter)

        startForeground(1, createNotification())
        startMonitoring()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenStateReceiver)
        serviceScope.cancel()
    }

    private fun stopMonitoring() {
        job?.cancel()
        job = null
    }

    private fun startMonitoring() {
        job = serviceScope.launch {
            while (isActive) {
                val foregroundApp = usageTracker.getForegroundApp()
                if (foregroundApp != null && foregroundApp != packageName) {
                    // 1. Record History
                    historyDao.insertHistory(UsageHistoryEntity(
                        packageName = foregroundApp,
                        timestamp = System.currentTimeMillis(),
                        duration = 3000, // Roughly 3s based on delay
                        hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    ))

                    // 2. Check Standard Rules
                    val activeRules = repository.getActiveRules()
                    for (rule in activeRules) {
                        if (ruleEngine.isRuleTriggered(rule, foregroundApp)) {
                            triggerAction(rule)
                        }
                    }

                    // 4. Autopilot Logic
                    val isAutopilotOn = autopilotPrefs.isAutopilotEnabled.first()
                    if (isAutopilotOn) {
                        val score = autopilotPrefs.disciplineScore.first()
                        val strictness = autopilotManager.getStrictnessLevel(score)
                        val history = historyDao.getAllHistory()
                        val dangerZones = PatternAnalyzer().detectDangerZones(history)
                        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

                        if (autopilotManager.shouldAutoBlock(foregroundApp, currentHour, dangerZones, strictness)) {
                            autopilotDao.insertLog(AutopilotLogEntity(
                                action = "AUTO_BLOCK",
                                reason = "Distraction detected during Danger Zone ($strictness Mode)"
                            ))
                            autopilotPrefs.updateDisciplineScore(-2) // Penalty for violation
                            triggerSmartIntervention("Autopilot: Blocking ${foregroundApp.split(".").last()} for your focus.")
                        } else {
                            autopilotPrefs.updateDisciplineScore(1) // Reward for focus
                        }
                    }
                }
                delay(3000) // Check every 3 seconds
            }
        }
    }

    private fun triggerSmartIntervention(message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "AUTOMIND_CHANNEL")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("AutoMind Insight")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(3, notification)
    }

    private fun triggerAction(rule: com.automind.data.model.RuleEntity) {
        // For demonstration, we'll show a system-level notification 
        // or potentially a full-screen overlay if permission is granted.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "AUTOMIND_CHANNEL")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Rule Triggered: ${rule.title}")
            .setContentText("Focus Mode: ${rule.description}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(2, notification)
        
        // In a real app, you might start an activity with Intent.FLAG_ACTIVITY_NEW_TASK 
        // to show a blocking screen.
    }

    private fun createNotification(): Notification {
        val channelId = "AUTOMIND_CHANNEL"
        val channel = NotificationChannel(channelId, "AutoMind Focus Service", NotificationManager.IMPORTANCE_LOW)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("AutoMind Active")
            .setContentText("Monitoring your focus rules...")
            .setSmallIcon(android.R.drawable.ic_lock_idle_lock)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
