package com.ivan.fgwallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ivan.fgwallet.utils.Constant;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ContactSupportActivity extends AppCompatActivity {
    public final String TAG = "VOLLEY";
    String tag_json_obj = "json_obj_req";
    KProgressHUD progress_dialog;

    EditText edtName;
    EditText edtEmail;
    EditText edtInqiry;
    EditText edtPassword;

    private void init() {
        edtName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.email);
        edtInqiry = findViewById(R.id.inquiry);
        edtPassword = findViewById(R.id.pass_word);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_support);
        getSupportActionBar().hide();
//        ButterKnife.inject(this);
        init();
        ImageView imageView = (ImageView) findViewById(R.id.menu);
        imageView.setImageResource(R.mipmap.backmenu);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(getResources().getString(R.string.contact_support));
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().equals("")) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please enter Name", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (edtEmail.getText().toString().equals("")) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please enter  Email", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (edtInqiry.getText().toString().equals("")) {
                    Snackbar.make(getWindow().getDecorView().getRootView(), "Please enter Inqiry", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    emailSupport();
                }
            }
        });
    }

    private void emailSupport() {
        String email = "android@financial-gate.info";
//        String email = "nhatpham.hitechltd@gmail.com";
//        String body = "Name: " + edtName.getText().toString() + "\n" +
//                "Email: " + edtEmail.getText().toString() + "\n" +
//                "Inquiry: " + edtInqiry.getText().toString() + "\n";
//        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        emailIntent.setData(Uri.parse("mailto:" + email));
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Support");
//        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(getApplicationContext(), "No email clients installed.", Toast.LENGTH_SHORT).show();
//        }

//        List<String> toEmailList = Arrays.asList(email
//                .split("\\s*,\\s*"));
//        new SendMailTask(ContactSupportActivity.this).execute(edtEmail.getText().toString(),
//                edtPassword.getText().toString(), toEmailList, "Contact Support",  edtInqiry.getText().toString());
        sendMail(email, "Contact Support", "Email: " + edtEmail.getText().toString() + "\n" + edtInqiry.getText().toString());
    }

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.yandex.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("fgwallet@yandex.com", "fgwallet123456");
            }
        });
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws
            MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("fgwallet@yandex.com", edtName.getText().toString()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }
    public class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(ContactSupportActivity.this, "Please wait", "Sending mail", true, false);
            progressDialog = new ProgressDialog(ContactSupportActivity.this);
            progressDialog.setMessage("Sending mail...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            showToast("Email Sent.");
        }

        protected Void doInBackground(javax.mail.Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void showToast(String message) {
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}