import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller {

    private static final String MY_NAME = "Anthony Aquino";
    private static final String MY_STATUS = "Cramming";
    private static final String MY_QUOTE = "Always do great stuff!";
    private static final String MY_PICTURE = "Aquino.jpeg";

    private static class Profile {
        String name, status, quote, picture;
        ObservableList<String> friends = FXCollections.observableArrayList();

        Profile(String name, String status, String quote, String picture) {
            this.name = name;
            this.status = status;
            this.quote = quote;
            this.picture = picture;
        }
    }

    private List<Profile> people = new ArrayList<>();
    private Profile me;
    private Profile current;

    @FXML private ListView<String> listView;
    @FXML private TextField nameField, statusChangeLabel, pictureLabel, friendLabel, unfriendLabel, quoteLabel;
    @FXML private Label statusLabel, nameLabel, currentStatusLabel, quoteDisplayLabel;
    @FXML private ImageView pictureView;

    @FXML
    public void initialize() {
        me = new Profile(MY_NAME, MY_STATUS, MY_QUOTE, MY_PICTURE);
        people.add(me);

        people.add(new Profile("Holden King Navio", "Swimming", "I love the ocean", "King.jpeg"));
        people.add(new Profile("Ryan Santos", "Working Out", "No pain no gain", "Ryan.jpeg"));
        people.add(new Profile("Marcus Cornejo", "In the Club", "I can save her", "Marcus.jpeg"));
        people.add(new Profile("Erick Padilla", "Running", "Keep Hustling", "Erick.jpeg"));
        people.add(new Profile("Margarette Baritua", "Fixing my car", "I love my Ford Fiesta", "Margs.jpeg"));


        listView.setOnMouseClicked(ev -> {
            String sel = listView.getSelectionModel().getSelectedItem();
            if (ev.getClickCount() == 2 && sel != null) show(find(sel));
        });
        show(me);
    }

    private Profile find(String name) {
        for (Profile p : people) {
            if (p.name.equalsIgnoreCase(name.trim())) return p;
        }
        return null;
    }

    private Image loadImage(String file) {
        URL url = getClass().getResource(file);
        if (url == null) url = getClass().getResource("unknown.png");
        return new Image(url.toExternalForm());
    }

    private void show(Profile p) {
        current = p;
        nameLabel.setText(p.name);
        currentStatusLabel.setText(p.status);
        quoteDisplayLabel.setText(p.quote);
        pictureView.setImage(loadImage(p.picture));
        listView.setItems(p.friends);
    }

    @FXML
    public void handleAdd(ActionEvent e) {
        String n = nameField.getText().trim();
        if (n.isEmpty()) return;
        if (find(n) != null) {
            statusLabel.setText(n + " already has an account");
            return;
        }
        people.add(new Profile(n, "New here", "No quote yet", "unknown.png"));
        statusLabel.setText("account created for " + n);
        nameField.clear();
    }

    @FXML
    public void handleDelete(ActionEvent e) {
        String n = nameField.getText().trim();
        Profile p = find(n);
        if (p == null) {
            statusLabel.setText(n + " not found");
            return;
        }
        if (p == me) {
            statusLabel.setText("you can't delete your own account");
            return;
        }
        people.remove(p);
        for (Profile q : people) q.friends.remove(p.name);
        if (current == p) show(me);
        statusLabel.setText(p.name + " deleted");
        nameField.clear();
    }

    @FXML
    public void handleLookup(ActionEvent e) {
        String n = nameField.getText().trim();
        Profile p = find(n);
        if (p == null) {
            statusLabel.setText(n + " not found");
            return;
        }
        show(p);
        statusLabel.setText("showing " + p.name + "'s profile");
    }

    @FXML
    public void handleStatus(ActionEvent e) {
        String s = statusChangeLabel.getText();
        current.status = s;
        currentStatusLabel.setText(s);
        statusLabel.setText("status changed to " + s);
        statusChangeLabel.clear();
    }

    @FXML
    public void handlePicture(ActionEvent e) {
        String file = pictureLabel.getText().trim();
        if (getClass().getResource(file) == null) {
            statusLabel.setText("picture not found: " + file);
            return;
        }
        current.picture = file;
        pictureView.setImage(loadImage(file));
        statusLabel.setText("picture changed to " + file);
        pictureLabel.clear();
    }

    @FXML
    public void handleAddFriend(ActionEvent e) {
        String f = friendLabel.getText().trim();
        Profile p = find(f);
        if (p == null) {
            statusLabel.setText(f + " has no account yet, create one with Name > Add");
            return;
        }
        if (p == current) {
            statusLabel.setText("you can't friend yourself");
            return;
        }
        if (current.friends.contains(p.name)) {
            statusLabel.setText(p.name + " is already a friend");
            return;
        }
        current.friends.add(p.name);
        p.friends.add(current.name);
        statusLabel.setText(p.name + " added as friend");
        friendLabel.clear();
    }

    @FXML
    public void handleUnfriend(ActionEvent e) {
        String f = unfriendLabel.getText().trim();
        Profile p = find(f);
        if (p == null || !current.friends.contains(p.name)) {
            statusLabel.setText(f + " is not a friend");
            return;
        }
        current.friends.remove(p.name);
        p.friends.remove(current.name);
        statusLabel.setText(p.name + " removed as friend");
        unfriendLabel.clear();
    }

    @FXML
    public void handleQuote(ActionEvent e) {
        String q = quoteLabel.getText();
        current.quote = q;
        quoteDisplayLabel.setText(q);
        statusLabel.setText("Quote changed to " + q);
        quoteLabel.clear();
    }
}