package com.vexonelite.fragmentleftmenu;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class LeftMenuFragment extends Fragment {

    //private static final String TAG = LeftMenuFragment.class.getSimpleName();

    private static final int ADD_INCOMING_LIST = 20001;
    private static final int ADAPTER_NOTIFY = 20002;

    private HashMap<String, MenuData> mMenuDataMap;

    private LeftMenuAdapter mAdapter;

    private Handler mUiHandler;

    private MenuActionCallback mCallback;

    public void setMenuActionCallback (MenuActionCallback callback) {
        mCallback = callback;
    }

    public interface MenuActionCallback {
        void onMenuItemClicked(Object info);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_left_menu, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (null == mMenuDataMap) {
            initMenuDataMap();
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (null != recyclerView) {
            setupRecyclerView(recyclerView);
        }

        if (null == mUiHandler) {
            mUiHandler = new MyHandler(Looper.getMainLooper());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapterList();
    }


    private void setupRecyclerView (RecyclerView recyclerView) {
        mAdapter = new LeftMenuAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setItemAnimator(null);
        //mRecyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        //recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, this));
    }

    private void initAdapterList() {
        Set<String> keySet = mMenuDataMap.keySet();
        List<String> menuList = new ArrayList<>(keySet);
        mAdapter.allNewDataSet(menuList, true);
    }

    private void initMenuDataMap () {
        mMenuDataMap = new HashMap<>();
        String[] titleSet = {
                "Home", "Facebook", "Twitter", "LINE", "Contact us", "About us"
        };
        for (int i = 0; i < titleSet.length; i++) {
            MenuData menuData = new MenuData();
            menuData.setMenuTitle(titleSet[i]);
            mMenuDataMap.put(titleSet[i], menuData);
        }
    }

    private abstract class BaseMenuItemClicker implements View.OnClickListener {

        Object mObject;

        public BaseMenuItemClicker (Object object) {
            mObject = object;
        }
    }

    private class LeftMenuAdapter
            extends AbstractRecycleListAdapter<String, RecyclerView.ViewHolder> {

        @Override
        public int getItemCount() {
            return this.getRealDataCount();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = LeftMenuFragment.this.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            return new MenuItemViewHolder(
                    inflater.inflate(R.layout.left_menu_item, parent, false) );
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            MenuItemViewHolder cell = (MenuItemViewHolder) viewHolder;
            cell.onBind(getObjectAtPosition(position), position);
        }
    }

    public class MenuItemViewHolder extends RecyclerView.ViewHolder {

        private ViewGroup itemCell;
        private TextView itemTitle;

        public MenuItemViewHolder(View itemView) {
            super(itemView);
            itemCell = (ViewGroup) itemView;
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
        }

        public void onBind (String dataKey, final int position) {
            resetItemViewLooks();

            MenuData data = null;
            try {
                data = LeftMenuFragment.this.mMenuDataMap.get(dataKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null == data) {
                return;
            }

            if (null != data.getMenuTitle()) {
                itemTitle.setText(data.getMenuTitle());
            }
            itemCell.setOnClickListener(new BaseMenuItemClicker(data) {
                @Override
                public void onClick(View v) {
                    if ( (null == mObject) || (null == LeftMenuFragment.this.mCallback) ) {
                        return;
                    }
                    LeftMenuFragment.this.mCallback.onMenuItemClicked(mObject);
                }
            });
        }

        private void resetItemViewLooks () {
            itemTitle.setText("");
            itemCell.setOnClickListener(null);
        }
    }

    private class MyHandler extends Handler {

        MyHandler (Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_INCOMING_LIST:
                    break;

                case ADAPTER_NOTIFY:
                    //if (null != LeftMenuFragment.this.mAdapter) {
                    //    LeftMenuFragment.this.mAdapter.notifyItemRangeChanged(msg.arg1, msg.arg2);
                    //}
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }



}
