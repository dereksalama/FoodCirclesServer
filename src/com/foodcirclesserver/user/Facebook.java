package com.foodcirclesserver.user;

//http://www.richardnichols.net/2010/06/implementing-facebook-oauth-2-0-authentication-in-java/
public class Facebook {
    // get these from your FB Dev App   
    private static final String secret = "2de4bebce24c38d34bf838a407ee2022";
    private static final String client_id = "562539180438611";  

    // set this to your servlet URL for the authentication servlet/filter
    private static final String redirect_uri = "http://localhost:8888/fboauth"; 
    /// set this to the list of extended permissions you want
    //private static final String[] perms = new String[] {"publish_stream", "email"};


    public static String getSecret() {
        return secret;
    }

    public static String getLoginRedirectURL() {
        return "https://graph.facebook.com/oauth/authorize?client_id=" + 
            client_id + "&display=page&redirect_uri=" + 
            redirect_uri+"&scope="; //+StringUtil.delimitObjectsToString(",", perms);
    }

    public static String getAuthURL(String authCode) {
        return "https://graph.facebook.com/oauth/access_token?client_id=" + 
            client_id+"&redirect_uri=" + 
            redirect_uri+"&client_secret="+secret+"&code="+authCode;
    }
    
    public static String getUserURL(String authCode) {
    	return "https://graph.facebook.com/me?&fields=id&access_token=" + authCode;
    	//"https://graph.facebook.com/oauth/me?access_token=" + authCode; gets all user data
    }
    
    public static String friendsURL(String userID, String accessToken) {
    	return "https://graph.facebook.com/" + userID + "/friends?access_token=" + accessToken;
    }
}