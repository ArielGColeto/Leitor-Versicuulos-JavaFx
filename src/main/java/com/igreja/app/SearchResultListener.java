package com.igreja.app;

import com.igreja.app.model.Verse;
import java.util.List;

public interface SearchResultListener {
    void onSearchResults(List<Verse> results);
}