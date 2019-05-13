package com.renyu.commonlibrary.dialog;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DialogManager {
    private static volatile DialogManager manager;
    private static ArrayList<HashMap<FragmentActivity, ChoiceDialog>> lists;

    public static DialogManager getInstance() {
        if (manager == null) {
            synchronized (DialogManager.class) {
                if (manager == null) {
                    manager = new DialogManager();
                    lists = new ArrayList<>();
                }
            }
        }
        return manager;
    }

    /**
     * 添加Dialog
     *
     * @param fragmentActivity
     * @param choiceDialog
     */
    public synchronized void addDialog(FragmentActivity fragmentActivity, ChoiceDialog choiceDialog) {
        HashMap<FragmentActivity, ChoiceDialog> map = new HashMap<>();
        choiceDialog.setOnDialogDismissListener(() -> {
            lists.remove(map);
            if (lists.size() != 0) {
                showDialog(lists.get(0));
            }
        });
        map.put(fragmentActivity, choiceDialog);
        if (lists.size() == 0) {
            lists.add(map);
            showDialog(map);
        } else {
            lists.add(map);
        }
    }

    private static void showDialog(HashMap<FragmentActivity, ChoiceDialog> map) {
        FragmentActivity fragmentActivity = null;
        ChoiceDialog choiceDialog = null;

        Iterator<Map.Entry<FragmentActivity, ChoiceDialog>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<FragmentActivity, ChoiceDialog> entry = it.next();
            fragmentActivity = entry.getKey();
            choiceDialog = entry.getValue();
        }
        if (fragmentActivity != null && choiceDialog != null) {
            choiceDialog.show(fragmentActivity);
        }
    }
}
