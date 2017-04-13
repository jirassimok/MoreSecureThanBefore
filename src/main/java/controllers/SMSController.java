package controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * Created by jtgaulin on 4/12/17.
 */
public class SMSController
{

	public SMSController(){
	String accountSid = "TWILIO_ACCOUNT_ID"; // Your Account SID from www.twilio.com/user/account
	String authToken = "TWILIO_AUTH_TOKEN"; // Your Auth Token from www.twilio.com/user/account

	Twilio.init(accountSid, authToken);

	Message message = Message.creator(
			new PhoneNumber("+15086851848"),  // To number
			new PhoneNumber("+15086199280"),  // From number
			"Hello world!"                    // SMS body
	).create();

	System.out.println(message.getSid());
	}
}
