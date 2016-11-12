package com.dyszlewskiR.edu.scientling.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Razjelll on 07.11.2016.
 */

public abstract class AndroidFileOpener {
    public abstract InputStream getStream(String fileName) throws IOException;
}
