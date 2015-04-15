package com.meetup.seii.meetup;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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
    final static String ADDRESS = "http://reidhoruff.webfactional.com";
//    final static String ADDRESS = "http://192.168.0.15:8000";

    public ServerCommunicator(ServerCommunicatable client) {
        this.client = client;
        this.httpclient = new DefaultHttpClient();
    }

    private static Uri.Builder getBaseURIBuilder(String action) {
        Uri.Builder builder = Uri.parse(ADDRESS).buildUpon();
        return builder.appendPath("api").appendQueryParameter("a", action);
    }

    public void createUser(MeetupUser user) {
        Uri.Builder builder = getBaseURIBuilder("create_user")
                .appendQueryParameter("password", user.password)
                .appendQueryParameter("username", user.username)
                .appendQueryParameter("firstname", user.firstName)
                .appendQueryParameter("lastname", user.lastName)
                .appendQueryParameter("sex", user.sex)
                .appendQueryParameter("age", user.age);

        new CreateUserRequestTask(this.client).execute(builder.build().toString());
    }

    public void fetchAllIntersts() {
        Uri.Builder builder = getBaseURIBuilder("list_all_interests");
        new GetAllInterestsRequestTask(this.client).execute(builder.build().toString());
    }

    public void loginUser(String username, String password) {
        Uri.Builder builder = getBaseURIBuilder("login")
                .appendQueryParameter("username", username)
                .appendQueryParameter("password", password);

        new LoginRequestTask(this.client).execute(builder.build().toString());
    }

    public void loginPrimaryUser() {
        MeetupUser primaryUser = MeetupSingleton.get().getUser();
        Uri.Builder builder = getBaseURIBuilder("login")
                .appendQueryParameter("username", primaryUser.username)
                .appendQueryParameter("password", primaryUser.password);

        new LoginRequestTask(this.client).execute(builder.build().toString());
    }

    public void updateInterestsOfUser(MeetupUser user) {
        Uri.Builder builder = getBaseURIBuilder("set_interests")
                .appendQueryParameter("username", user.username)
                .appendQueryParameter("password", user.password);

        String ids = "";
        boolean first = true;
        for (Map.Entry<String, Interest> entry : user.getInterestMap().entrySet()) {
            if (!first) ids += ",";
            ids += ((Interest)entry.getValue()).id;
            first = false;
        }

        builder.appendQueryParameter("ids", ids);

        new UpdateInterestsRequestTask(this.client).execute(builder.build().toString());
    }

    public void updateInterestsOfMainUser() {
        MeetupUser user = MeetupSingleton.get().getUser();
        Uri.Builder builder = getBaseURIBuilder("set_interests")
                .appendQueryParameter("username", user.username)
                .appendQueryParameter("password", user.password);

        String ids = "";
        boolean first = true;
        for (Map.Entry<String, Interest> entry : user.getInterestMap().entrySet()) {
            if (!first) ids += ",";
            ids += ((Interest)entry.getValue()).id;
            first = false;
        }

        builder.appendQueryParameter("ids", ids);

        new UpdateInterestsRequestTask(this.client).execute(builder.build().toString());
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
        Log.i("REST", uri[0]);

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
                return null;
            }
        } catch (ClientProtocolException e) {
            Log.i("REST", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.i("REST", e.getMessage());
            return null;
        }

        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        JSONParser parser = new JSONParser();
        JSONObject jsonobj = null;
        ResponseStatus status = ResponseStatus.SUCCESS;

        if (result != null) {
            try {
                jsonobj = (JSONObject)(parser.parse(result));
                status = ResponseStatus.SUCCESS;
            } catch (ParseException e) {
                status = ResponseStatus.PARSE_FAILED;
                Log.i("REST", "failed to parse result");
            }
        } else {
            status = ResponseStatus.FAILED_TO_CONNECT;
        }

        if (jsonobj != null) {
            this.isSuccess = (Boolean)jsonobj.get("success");
        }

        this.notify(status, jsonobj);
    }

    protected boolean isSuccess() {
        return this.isSuccess;
    }

    abstract protected void notify(ResponseStatus status, JSONObject json);
}

class CreateUserRequestTask extends RequestTask {
    public CreateUserRequestTask(ServerCommunicatable activity) {
        super(activity);
    }

    @Override
    protected void notify(ResponseStatus status, JSONObject json) {
        if (status == ResponseStatus.SUCCESS) {
            this.activity.createUserResponse(
                    status,
                    this.isSuccess(),
                    (String)json.get("msg"));
        } else {
            this.activity.createUserResponse(status, false, null);
        }
    }
}

class GetAllInterestsRequestTask extends RequestTask {
    public GetAllInterestsRequestTask(ServerCommunicatable activity) {
        super(activity);
    }

    @Override
    protected void notify(ResponseStatus status, JSONObject json) {
        if (status == ResponseStatus.SUCCESS) {
            ArrayList<Interest> interests = new ArrayList<Interest>();
            JSONObject data = (JSONObject)json.get("data");
            Iterator iterator = data.keySet().iterator();
            MeetupSingleton.get().clearAllInterests();

            while (iterator.hasNext()) {
                String key = (String)iterator.next();
                String value = (String)data.get(key);
                MeetupSingleton.get().clearAllInterests();
                Log.i("REST", "adding activity: " + value);
                MeetupSingleton.get().addInterest(new Interest(key, value));
            }
            this.activity.listAllInterestsResponse(status, interests);
        } else {
            this.activity.listAllInterestsResponse(status, null);
        }
    }
}

class LoginRequestTask extends RequestTask {
    public LoginRequestTask(ServerCommunicatable activity) {
        super(activity);
    }

    @Override
    protected void notify(ResponseStatus status, JSONObject json) {
        if (status == ResponseStatus.SUCCESS && this.isSuccess()) {
            JSONObject data = (JSONObject)json.get("data");
            this.activity.loginResponse(status, new MeetupUser(data));
        } else {
            this.activity.loginResponse(status, null);
        }
    }
}

class UpdateInterestsRequestTask extends RequestTask {
    public UpdateInterestsRequestTask(ServerCommunicatable activity) {
        super(activity);
    }

    @Override
    protected void notify(ResponseStatus status, JSONObject json) {
        this.activity.updateInterestsResponse(status, this.isSuccess());
    }
}
