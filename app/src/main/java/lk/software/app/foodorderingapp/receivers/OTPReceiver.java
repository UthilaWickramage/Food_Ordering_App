package lk.software.app.foodorderingapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.EditText;

public class OTPReceiver extends BroadcastReceiver {
    EditText editText;
    Button button;

    public OTPReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] sms = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for(SmsMessage smsMessage : sms){
            String messageBody = smsMessage.getMessageBody();
            String s = messageBody.split(" ")[0];
            editText.setText(s);

            clickBtn(button);

        }
    }

    private void clickBtn(Button button) {
        button.performClick();
    }


    public void setEditText(EditText editText,Button button){
        this.editText = editText;
        this.button = button;
    }

}
