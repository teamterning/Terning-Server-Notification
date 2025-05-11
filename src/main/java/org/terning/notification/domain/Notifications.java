package org.terning.notification.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Notifications {

    private final List<Notification> notifications;

    private Notifications(List<Notification> notifications) {
        this.notifications = List.copyOf(notifications);
    }

    public static Notifications of(List<Notification> notifications) {
        return new Notifications(notifications);
    }

    public static Notifications empty() {
        return new Notifications(Collections.emptyList());
    }

    public List<Notification> values() {
        return Collections.unmodifiableList(notifications);
    }

    public Notifications add(Notification notification) {
        List<Notification> newList = new ArrayList<>(this.notifications);
        newList.add(notification);
        return new Notifications(newList);
    }

    public Notifications remove(Notification notification) {
        List<Notification> newList = new ArrayList<>(this.notifications);
        newList.remove(notification);
        return new Notifications(newList);
    }
}
