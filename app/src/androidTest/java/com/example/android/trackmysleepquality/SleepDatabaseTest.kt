package com.example.android.trackmysleepquality

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@SmallTest
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        sleepDao = db.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight()  = runBlocking{
        val night = SleepNight()
        sleepDao.insert(night)
        val tonight = sleepDao.getLastNight()
        assertEquals(tonight?.sleepQuality, -1)
    }

    @Test
    @Throws(Exception::class)
    fun clear() = runBlocking{
        val night = SleepNight()
        val night2 = SleepNight()
        val night3 = SleepNight()
        sleepDao.insert(night)
        sleepDao.insert(night2)
        sleepDao.insert(night3)
        sleepDao.clear()
        val tonight = sleepDao.getLastNight()
        assertEquals(tonight, null)
    }

    @Test
    @Throws(Exception::class)
    fun getNight() = runBlocking{
        val night = SleepNight()
        val night2 = SleepNight()
        val night3 = SleepNight()
        sleepDao.insert(night)
        sleepDao.insert(night2)
        sleepDao.insert(night3)
        val nigth = sleepDao.get(1L)
        assertEquals(nigth?.nightId, 1L)
    }
}
