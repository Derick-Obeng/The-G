
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import toolbarButtonGraphics.JSONArray;

public class TheGit {
    private static final String GITHUB_API_URL = "https://api.github.com/users/";
    private static ResourceBundle event;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java GitHubActivityCLI <username>");
            return;
        }

        String username = args[0];
        fetchGitHubActivity(username);
    }

    // Fetch GitHub activity
    private static void fetchGitHubActivity(String username) {
        try {
            String urlString = "https://api.github.com/users/" + username + "/events";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check if the request was successful
            if (connection.getResponseCode() != 200) {
                System.out.println("Error fetching data from GitHub API. Response code: " + connection.getResponseCode());
                return;
            }

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse and display the events
            displayActivity(response.toString());

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    // Display the GitHub activity
    private static void displayActivity(String jsonResponse) {
        try {
            // Parse the JSON response
            toolbarButtonGraphics.JSONArray events = new JSONArray(jsonResponse);

            if (events.length() == 0) {
                System.out.println("No recent activity found.");
                return;
            }

            // Display the recent activity
            System.out.println("Recent activity:");

            for (int i = 0; i < events.length(); i++) {
               

                // Check the type of event and display relevant information
                ;
                String type = event.getString("type");
                String repoName = event.getObject("repo").toString();

                switch (type) {
                    case "PushEvent":
                        Class<?>[] pushCount;
                        pushCount = event.getStringArray("payload").getClass().getInterfaces();
                        System.out.println("Pushed " + pushCount + " commits to " + repoName);
                        break;
                    case "IssuesEvent":
                        String action = event.getObject("payload").toString();
                        System.out.println(action.substring(0, 1).toUpperCase() + action.substring(1) + " an issue in " + repoName);
                        break;
                    case "WatchEvent":
                        System.out.println("Starred " + repoName);
                        break;
                    default:
                        System.out.println(type + " event in " + repoName);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing the activity data: " + e.getMessage());
        }
    }

    private static class JSONArray extends toolbarButtonGraphics.JSONArray {
        public JSONArray(String jsonResponse) {
            super();
        }
    }
}
