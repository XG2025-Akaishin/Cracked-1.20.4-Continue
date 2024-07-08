package me.alpha432.oyvey.features.modules.chat.chatmodifier.utils;

public class Timer {
   private long time = -1L;
   private long current;
   private long lastMS = 0L;

   public boolean passedS(double s) {
      return this.passedMs((long)s * 1000L);
   }

   public boolean passedDms(double dms) {
      return this.passedMs((long)dms * 10L);
   }

   public boolean passedDs(double ds) {
      return this.passedMs((long)ds * 100L);
   }

   public boolean passedD(double dms) {
      return this.getMs(System.nanoTime() - this.time) >= (long)(dms * 3.0D);
   }

   public boolean passedMs(long ms) {
      return this.passedNS(this.convertToNS(ms));
   }

   public void setMs(long ms) {
      this.time = System.nanoTime() - this.convertToNS(ms);
   }

   public void setTimePassed(long ms) {
      this.time = System.nanoTime() - ms * 1000000L;
   }

   public boolean passedNS(long ns) {
      return System.nanoTime() - this.time >= ns;
   }

   public long getPassedTimeMs() {
      return this.getMs(System.nanoTime() - this.time);
   }

   public Timer reset() {
      this.time = System.nanoTime();
      return this;
   }

   public void reset2() {
      this.current = System.currentTimeMillis();
   }

   public long getMs(long time) {
      return time / 1000000L;
   }

   public long convertToNS(long time) {
      return time * 1000000L;
   }

   public boolean sleep(long l) {
      if (System.nanoTime() / 1000000L - l >= l) {
         this.reset();
         return true;
      } else {
         return false;
      }
   }

   public boolean hasReached(long passedTime) {
      return System.currentTimeMillis() - this.current >= passedTime;
   }

   public boolean passed(double ms) {
      return (double)(System.currentTimeMillis() - this.current) >= ms;
   }
}
