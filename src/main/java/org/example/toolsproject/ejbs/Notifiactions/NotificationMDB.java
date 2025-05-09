package org.example.toolsproject.ejbs.Notifiactions;


   import jakarta.annotation.Resource;
   import jakarta.ejb.ActivationConfigProperty;
   import jakarta.ejb.MessageDriven;
   import jakarta.inject.Inject;
   import jakarta.jms.Message;
   import jakarta.jms.MessageListener;
   import jakarta.jms.ObjectMessage;
   import jakarta.persistence.EntityManager;
   import jakarta.persistence.PersistenceContext;
   import org.example.toolsproject.models.Notifications.Notification;
   import org.example.toolsproject.models.Notifications.NotificationEvent;


@MessageDriven(activationConfig = {
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
            @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/NotificationQueue")
    })
    public class NotificationMDB implements MessageListener {
        @PersistenceContext
        private EntityManager em;

        @Override
        public void onMessage(Message message) {
            try {
                if (message instanceof ObjectMessage) {
                    ObjectMessage objMessage = (ObjectMessage) message;
                    NotificationEvent event = (NotificationEvent) objMessage.getObject();
                    System.out.println("Received Notification: " + event.getEventType() + " for User: " + event.getUserId());

                    // Store in database
                    Notification notification = new Notification(
                            event.getUserId(),
                            event.getEventType(),
                            event.getRelatedUserId(),
                            event.getTaskUpdates()
                    );
                    em.persist(notification);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }