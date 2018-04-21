
import no.vianett.sms.SmsEventListener;
import no.vianett.sms.Sms;
import no.vianett.sms.SmsEvent;
import no.vianett.sms.component.SmsTransceiver;
import no.vianett.sms.log.SmsScreenLogger;
import no.vianett.sms.event.SmsDeliveredEvent;
import no.vianett.sms.event.SmsSendingFailedEvent;
import no.vianett.sms.event.SmsDeliveryFailedEvent;

public class SmsSender implements SmsEventListener
{
    private SmsTransceiver transceiver = null;
    private Object link = null; // Just to keep this object alive.
    private int counter = 0;

    public SmsSender()
    {
        this.link = this; // Keeps this object alive.
        this.transceiver = SmsTransceiver.getInstance(); // Get the transceiver object.

        // Initialize transceiver.
        String smsHost = "cpa.vianett.no";
        String smsPort = "31337";
        String smsUsername = "username";
        String smsPassword = "password";
        this.transceiver.initialize( smsHost, Integer.parseInt( smsPort ), smsUsername, smsPassword, new SmsScreenLogger() );

        this.transceiver.addSmsEventListener( this ); // Registrer this class as listener for SMS events.

        // Send message
        Sms sms = new Sms();
        sms.setId( ++this.counter );
        sms.setReplyPath( 100 );
        sms.setSender( "1963" ); // Set the sender number.
        sms.setMessage( "Hello World!" );
        sms.setRecipient( "xxxxxxxx" ); // The recipients phone number.

        this.transceiver.send( sms );
    }

    // Just make new instance of this class.
    public static void main( String[] args ){ new SmsSender(); }

    /**
     * Listener.
     *
     * @param event a <code>no.vianett.sms.SmsEvent</code>
     */
    public void eventHappened( SmsEvent event )
    {
        if( event instanceof SmsDeliveredEvent )
        {
            System.out.println( "Sms delivered." );
            System.out.println( "Refno : " + event.getReferenceId() );
            System.out.println( "Sms generated by : " + event.getSource().getClass() );
        }
        else if( event instanceof SmsSendingFailedEvent )
        {
            System.out.println( "Sms sending failed." );
            System.out.println( "Refno : " + event.getReferenceId() );
            System.out.println( "Return code : " + ( ( SmsSendingFailedEvent ) event ).getReturnCode() );
            System.out.println( "Sms generated by : " + event.getSource().getClass() );
        }
        else if( event instanceof SmsDeliveryFailedEvent )
        {
            System.out.println( "Sms delivery failed." );
            System.out.println( "Refno : " + event.getReferenceId() );
            System.out.println( "Error code : " + ( ( SmsDeliveryFailedEvent ) event ).getErrorCode() );
            System.out.println( "Sms generated by : " + event.getSource().getClass() );
        }
    }

}
