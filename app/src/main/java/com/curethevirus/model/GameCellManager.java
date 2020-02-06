package com.curethevirus.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameCellManager implements Iterable<GameCell> {

    private List<GameCell> cellList = new ArrayList<>();


    @Override
    public Iterator<GameCell> iterator() {
        return cellList.iterator();
    }
}
