package com.example.gags.GetItAssignment;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gags on 02/08/15.
 */
public class NotificationID {
        private final static AtomicInteger c = new AtomicInteger(0);
        public static int getID() {
            return c.incrementAndGet();
        }

}
