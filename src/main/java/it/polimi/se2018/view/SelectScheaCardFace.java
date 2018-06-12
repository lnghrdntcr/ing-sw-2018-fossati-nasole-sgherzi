package it.polimi.se2018.view;

public abstract class SelectScheaCardFace {
    private RemoteView view;

    public SelectScheaCardFace(RemoteView view) {
        this.view = view;
    }

    protected final void selectSchemaCardFace(int index){
        //TODO
    }

    public abstract void showSchemaCardFaceSelection();
}
