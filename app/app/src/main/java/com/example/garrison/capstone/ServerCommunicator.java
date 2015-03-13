package com.example.garrison.capstone;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * each activity that needs to communicate with the server (and
 * subsiquently implements ServerCommunicator) should have a single instance
 * of ServerCommunicator. ServerCommunicator is the object through which
 * actual server calls are made. The appropriate function implemented by
 * the calling activity (mandated by ServerCommunicator) will be called
 * when the request completes or fails.
 */
public class ServerCommunicator {
    HttpClient httpclient = null;
    private ServerCommunicatable client;
    final static String ADDRESS = "192.168.0.11:8000";
    private String devID = null;

    public ServerCommunicator(ServerCommunicatable client) {
        this.client = client;
        this.httpclient = new DefaultHttpClient();
    }

    private static Uri.Builder getBaseURIBuilder(String action) {
        return new Uri.Builder().scheme("http").authority(ADDRESS).appendPath("api").appendQueryParameter("a", action);
    }

    public void ping() {
        Uri.Builder builder = getBaseURIBuilder("ping");
        new TestPingRequestTask(this.client).execute(builder.build().toString());
    }

    public void createUser(String password, String password2, MeetupUser user) {
        Uri.Builder builder = getBaseURIBuilder("create_user")
                .appendQueryParameter("password", password)
                .appendQueryParameter("password2", password2)
                .appendQueryParameter("username", user.username)
                .appendQueryParameter("firstname", user.firstName)
                .appendQueryParameter("lastname", user.lastName)
                .appendQueryParameter("sex", user.sex)
                .appendQueryParameter("age", Integer.toString(user.age));

        new CreateUserRequestTask(this.client).execute(builder.build().toString());
    }
}

/**
 * abstract class that handles the actual HTTP request,
 * when the request completes (or fails) the implementor is
 * notified via notify() which must be implemented. The implementation of
 * this method will typically notify the client activity via the
 * appropriate method in the ServerCommunicatable interface.
 */
abstract class RequestTask extends AsyncTask<String, String, String>{
    ServerCommunicatable activity;
    protected boolean isSuccess;

    public RequestTask(ServerCommunicatable activity) {
        this.activity = activity;
        this.isSuccess = false;
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        Log.v("REST", uri[0]);

        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                Log.i("REST", responseString);
            } else{
                Log.i("REST", "error----");
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        JSONParser parser = new JSONParser();
        JSONObject jsonobj = null;

        if (result != null) {
            try {
                jsonobj = (JSONObject)(parser.parse(result));
            } catch (ParseException e) {
                Log.i("REST", "failed to parse result");
            }
        }

        if (jsonobj != null) {
            this.isSuccess = (Boolean)jsonobj.get("success");
        }
        this.notify(jsonobj);
    }


    protected boolean isSuccess() {
        return this.isSuccess;
    }

    abstract protected void notify(JSONObject json);
}

class TestPingRequestTask extends RequestTask {
    public TestPingRequestTask(ServerCommunicatable activity) {
        super(activity);
    }

    @Override
    protected void notify(JSONObject json) {
        this.activity.pingResponse(this.isSuccess());
    }
}

class CreateUserRequestTask extends RequestTask {
    public CreateUserRequestTask(ServerCommunicatable activity) {
        super(activity);
    }

    @Override
    protected void notify(JSONObject json) {
        this.activity.createUserResponse(this.isSuccess(), (String)json.get("msg"));
    }
}

class FetchAllInterestsRequestTask extends RequestTask {
    public FetchAllInterestsRequestTask(ServerCommunicatable activity) {
        super(activity);
    }

    @Override
    protected void notify(JSONObject json) {
        //...
    }
}
