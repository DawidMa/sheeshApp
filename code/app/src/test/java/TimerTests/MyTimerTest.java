package TimerTests;

import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.dhkarlsruhe.it.sheeshapp.sheeshapp.MyTimer;

public class MyTimerTest {
    @Test
    public void assureCorrectTiming() {
        MyTimer timer1 = new MyTimer(36000, true);

        assertTrue(timer1.getMinutes() == 600);
        assertTrue(timer1.getSeconds() == 0);
        timer1.incSeconds();
        assertTrue(timer1.getSeconds() == 1);

        MyTimer timer2 = new MyTimer(600, true);
        assertTrue(timer2.getMinutes() == 10);
        assertTrue(timer2.getSeconds() == 0);
        timer1.decSeconds();
        assertTrue(timer2.getMinutes() == 9);
        assertTrue(timer2.getSeconds() == 59);
    }
}
