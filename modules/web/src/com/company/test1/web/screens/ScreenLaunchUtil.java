package com.company.test1.web.screens;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.builders.EditorBuilder;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;

public class ScreenLaunchUtil {

    public static void launchNewEntityStreen(Class c, String screenId, ScreenBuilders screenBuilders, FrameOwner origin, OpenMode openMode, DataContext parentDataContext, ScreenLaunchUtilCloseListener scl){
        EditorBuilder eb = screenBuilders.editor(c, origin)
                .withLaunchMode(openMode)
                .newEntity();
        if (parentDataContext!=null){
            eb.withParentDataContext(parentDataContext);
        }
        if (screenId!=null){
            eb.withScreenId(screenId);
        }

        Screen s = eb.build().show();

        s.addAfterCloseListener(event2->{
            StandardCloseAction ac = (StandardCloseAction) event2.getCloseAction();
            if (ac.getActionId().compareTo("commit")==0){
                if (scl!=null){
                    scl.screenClosed(s);
                }

            }
            int y = 2;
        });
    }

    public static void launchNewEntityStreen(Class c, String screenId, ListComponent list, ScreenBuilders screenBuilders, FrameOwner origin, OpenMode openMode, DataContext parentDataContext, ScreenLaunchUtilCloseListener scl){
        EditorBuilder eb = screenBuilders.editor(c, origin)
                .withLaunchMode(openMode)
                .newEntity();
        if (parentDataContext!=null){
            eb.withParentDataContext(parentDataContext);
        }
        if (screenId!=null){
            eb.withScreenId(screenId);
        }

        Screen s = eb.build().show();

        s.addAfterCloseListener(event2->{
            StandardCloseAction ac = (StandardCloseAction) event2.getCloseAction();
            if (ac.getActionId().compareTo("commit")==0){
                if (scl!=null){
                    scl.screenClosed(s);
                }

            }
            int y = 2;
        });
    }





    public static void launchNewEntityStreen(Class c, String screenId, ListComponent listComponent, ScreenBuilders screenBuilders, FrameOwner origin, OpenMode openMode, DataContext parentDataContext, ScreenLaunchUtilCloseListener scl, ScreenLaunchUtilAfterBuildListener sab){
        EditorBuilder eb = screenBuilders.editor(c, origin)
                .withLaunchMode(openMode)
                .newEntity();
        if (parentDataContext!=null){
            eb.withParentDataContext(parentDataContext);
        }
        if (screenId!=null){
            eb.withScreenId(screenId);
        }
        if (listComponent!=null){
            eb.withListComponent(listComponent);
        }

        Screen s = eb.build();

        if (sab!=null){
            sab.screenBuilt(s);
        }

        s.show();

        s.addAfterCloseListener(event2->{
            StandardCloseAction ac = (StandardCloseAction) event2.getCloseAction();
            if (ac.getActionId().compareTo("commit")==0){
                if (scl!=null){
                    scl.screenClosed(s);
                }

            }
            int y = 2;
        });
    }


    public static void launchNewEntityStreen(Class c, ScreenBuilders screenBuilders, FrameOwner origin, OpenMode openMode, DataContext parentDataContext, ScreenLaunchUtilCloseListener scl){
        EditorBuilder eb = screenBuilders.editor(c, origin)
                .withLaunchMode(openMode)
                .newEntity();
            if (parentDataContext!=null){
                eb.withParentDataContext(parentDataContext);
            }

        Screen s = eb.build().show();

        s.addAfterCloseListener(event2->{
            StandardCloseAction ac = (StandardCloseAction) event2.getCloseAction();
            if (ac.getActionId().compareTo("commit")==0){
                if (scl!=null){
                    scl.screenClosed(s);
                }

            }
            int y = 2;
        });
    }

    public static <T extends StandardEntity> void launchEditEntityScreen(T e, String screenId, ListComponent listComponent, ScreenBuilders screenBuilders, FrameOwner origin, OpenMode openMode, DataContext parentDataContext, ScreenLaunchUtilCloseListener scl){
        EditorBuilder eb = screenBuilders.editor(e.getClass(), origin)
                .withLaunchMode(openMode);


        if (parentDataContext!=null){
            eb.withParentDataContext(parentDataContext);
        }
        if (screenId!=null){
            eb.withScreenId(screenId);
        }
        if (listComponent!=null){
            eb.withListComponent(listComponent);
        }
        
        eb.editEntity(e);
        Screen s = eb.build();

        s.show();

        s.addAfterCloseListener(event2->{
            StandardCloseAction ac = (StandardCloseAction) event2.getCloseAction();
            if (ac.getActionId().compareTo("commit")==0){
                if (scl!=null){
                    scl.screenClosed(s);
                }

            }
            int y = 2;
        });
    }

    public static <T extends StandardEntity> void launchEditEntityScreen(T e, String screenId, ListComponent listComponent, ScreenBuilders screenBuilders, FrameOwner origin, OpenMode openMode, DataContext parentDataContext, ScreenLaunchUtilCloseListener scl, ScreenLaunchUtilAfterBuildListener sab){
        EditorBuilder eb = screenBuilders.editor(e.getClass(), origin)
                .withLaunchMode(openMode);


        if (parentDataContext!=null){
            eb.withParentDataContext(parentDataContext);
        }
        if (screenId!=null){
            eb.withScreenId(screenId);
        }
        if (listComponent!=null){
            eb.withListComponent(listComponent);
        }

        eb.editEntity(e);
        Screen s = eb.build();

        if (sab!=null){
            sab.screenBuilt(s);
        }

        s.show();

        s.addAfterCloseListener(event2->{
            StandardCloseAction ac = (StandardCloseAction) event2.getCloseAction();
            if (ac.getActionId().compareTo("commit")==0){
                if (scl!=null){
                    scl.screenClosed(s);
                }

            }
            int y = 2;
        });
    }

    public static <T extends StandardEntity> void launchEditEntityScreen(T e, ScreenBuilders screenBuilders, FrameOwner origin, OpenMode openMode, DataContext parentDataContext, ScreenLaunchUtilCloseListener scl){
        EditorBuilder eb = screenBuilders.editor(e.getClass(), origin)
                .withLaunchMode(openMode);

        if (parentDataContext!=null){
            eb.withParentDataContext(parentDataContext);
        }
        eb.editEntity(e);
        Screen s = eb.build();

        s.show();

        s.addAfterCloseListener(event2->{
            StandardCloseAction ac = (StandardCloseAction) event2.getCloseAction();
            if (ac.getActionId().compareTo("commit")==0){
                if (scl!=null){
                    scl.screenClosed(s);
                }

            }
            int y = 2;
        });
    }



}
