package br.com.brasolia.models;

import java.util.List;

/**
 * Created by cayke on 24/10/17.
 */

public interface BSDataUpdated {
    void categoriesUpdated(boolean success, List<BSCategory> categories);
}
