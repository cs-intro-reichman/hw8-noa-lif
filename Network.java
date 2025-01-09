
/** Represents a social network. The network has users, who follow other uesrs.
 *  Each user is an instance of the User class. */
public class Network {

    // Fields
    private User[] users;  // the users in this network (an array of User objects)
    private int userCount; // actual number of users in this network

    /**
     * Creates a network with a given maximum number of users.
     */
    public Network(int maxUserCount) {
        this.users = new User[maxUserCount];
        this.userCount = 0;
    }

    /**
     * Creates a network with some users. The only purpose of this constructor
     * is to allow testing the toString and getUser methods, before implementing
     * other methods.
     */
    public Network(int maxUserCount, boolean gettingStarted) {
        this(maxUserCount);
        users[0] = new User("Foo");
        users[1] = new User("Bar");
        users[2] = new User("Baz");
        userCount = 3;
    }

    public int getUserCount() {
        return this.userCount;
    }

    /**
     * Finds in this network, and returns, the user that has the given name. If
     * there is no such user, returns null. Notice that the method receives a
     * String, and returns a User object.
     */
    public User getUser(String name) {
        for (int i = 0; i < this.users.length; i++) {
            if (users[i] != null) {
                String userName = users[i].getName();
                if (userName.equals(name)) {
                    return users[i];
                }
            }
        }
        return null;
    }

    /**
     * Adds a new user with the given name to this network. If ths network is
     * full, does nothing and returns false; If the given name is already a user
     * in this network, does nothing and returns false; Otherwise, creates a new
     * user with the given name, adds the user to this network, and returns
     * true.
     */
    public boolean addUser(String name) {
        if (firstNullCellInUsers() == -1) {
            return false;
        }
        for (int i = 0; i < users.length; i++) {
            User currentUser = users[i];
            if (currentUser != null) {
                if (currentUser.getName().equals(name)) {
                    return false;
                }
            }
        }
        User newUser = new User(name);
        users[firstNullCellInUsers()] = newUser;
        userCount++;
        return true;
    }

    public int firstNullCellInUsers() {
        for (int i = 0; i < users.length; i++) {
            if (users[i] == null) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Makes the user with name1 follow the user with name2. If successful,
     * returns true. If any of the two names is not a user in this network, or
     * if the "follows" addition failed for some reason, returns false.
     */
    public boolean addFollowee(String name1, String name2) {
        if (getUser(name1) == null || getUser(name2) == null) {
            return false;
        }

        User user1 = getUser(name1);
        User user2 = getUser(name2);

        boolean user1FollowsUser2 = user1.follows(name2);
        boolean user2FollowsUser1 = user2.follows(name1);

        boolean indicator = true;
        if (!user1FollowsUser2) {
            if (!user1.addFollowee(name2)) {
                indicator = false;
            }
        }
        if (!user2FollowsUser1) {
            if (!user2.addFollowee(name1)) {
                indicator = false;
            }
        }
        return indicator;
    }

    /**
     * For the user with the given name, recommends another user to follow. The
     * recommended user is the user that has the maximal mutual number of
     * followees as the user with the given name.
     */
    public String recommendWhoToFollow(String name) {
        if (getUser(name) == null) {
            return null;
        }
        int maxMutualFollowers = 0;
        User user = getUser(name);
        String recommendedUser = null;
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                if (!users[i].getName().equals(name)) {
                    int mutualFollowers = users[i].countMutual(user);
                    if (maxMutualFollowers < mutualFollowers) {
                        maxMutualFollowers = mutualFollowers;
                        recommendedUser = users[i].getName();
                    }
                }
            }
        }
        return recommendedUser;
    }

    /**
     * Computes and returns the name of the most popular user in this network:
     * The user who appears the most in the follow lists of all the users.
     */
    public String mostPopularUser() {
        if (users[0] == null) {
            return null;
        }
        String[] firstUserFollows = users[0].getfFollows();
        String popularUser = "";
        if (firstUserFollows[0] != null) {
            popularUser = firstUserFollows[0];
        }
        int maxFollowers = 1;
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                int numOfFollowers = followeeCount(users[i].getName());
                if (numOfFollowers > maxFollowers) {
                    maxFollowers = numOfFollowers;
                    popularUser = users[i].getName();
                }
            }
        }
        return popularUser;
    }

    /**
     * Returns the number of times that the given name appears in the follows
     * lists of all the users in this network. Note: A name can appear 0 or 1
     * times in each list.
     */
    private int followeeCount(String name) {
        int counter = 0;
        for (int j = 0; j < users.length; j++) {
            if (users[j] != null) {
                if (users[j].follows(name)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    // Returns a textual description of all the users in this network, and who they follow.
    public String toString() {
        String output = "";
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                output += users[i].toString();
            }
        }
        return output;
    }
}
