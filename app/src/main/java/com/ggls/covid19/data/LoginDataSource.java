package com.ggls.covid19.data;

import com.ggls.covid19.UserDataBase;
import com.ggls.covid19.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            UserDataBase userDataBase = new UserDataBase();
            userDataBase.preLogin(username, password);
            if (userDataBase.userLogin()) {
                LoggedInUser loggedInUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username
                        );
                return new Result.Success<>(loggedInUser);
            } else {
                return null;
            }

            //
//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            java.util.UUID.randomUUID().toString(),
//                            "Jane Doe");
//            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}