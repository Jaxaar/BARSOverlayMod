package com.mojang.api.profiles;

import java.io.IOException;

public interface ProfileRepository {

    public Profile findProfileByName(String name) throws IOException;

}
