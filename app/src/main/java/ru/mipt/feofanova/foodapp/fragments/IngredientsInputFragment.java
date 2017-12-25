package ru.mipt.feofanova.foodapp.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rey.material.widget.ProgressView;

import java.util.ArrayList;

import ru.mipt.feofanova.foodapp.HttpGetRequestTask;
import ru.mipt.feofanova.foodapp.R;
import ru.mipt.feofanova.foodapp.RequestUrlCreator;

import static ru.mipt.feofanova.foodapp.NavigationActivity.fragment;
import static ru.mipt.feofanova.foodapp.NavigationActivity.mFragmentManager;


public class IngredientsInputFragment extends Fragment implements HttpGetRequestTask.IResponseListener
{
    private static final String INGREDIENTS_KEY_ = "INGREDIENTS";

    private EditText mEditText;
    private Button mFindButton;
    private String reqBody;
    private IngredientsInputFragment.mAdapter mAdapter;
    private HttpGetRequestTask req;
    private final ArrayList<String> ingredients = new ArrayList<>();
    private ProgressView mProgressView;
    ItemTouchHelper.SimpleCallback simpleCallback;
    final static String TAG_1 = "FRAGMENT_1";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private String basicUrl;
    AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients_input, container,
                false);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AppCompatActivity) getActivity();

        simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                final String deletedItem = mAdapter.mDataSet.get(position);

                Snackbar snackbar = Snackbar
                        .make(mActivity.findViewById(R.id.ingredients_relative), mAdapter.mDataSet.get(position) + " deleted!", Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAdapter.mDataSet.add(position, deletedItem);
                        mAdapter.notifyItemInserted(position);
                    }
                });
                mAdapter.mDataSet.remove(position);
                mAdapter.notifyItemRemoved(position);
                snackbar.show();
            }
        };
    }

    @Override
    public void onStart()
    {
        super.onStart();

        basicUrl = "";
        mEditText = mActivity.findViewById(R.id.edit_text);
        mRecyclerView = mActivity.findViewById(R.id.recipes_recycler_view);
        mProgressView = mActivity.findViewById(R.id.ingredients_progress_view);
        mFindButton = mActivity.findViewById(R.id.find_button);

        NavigationView navigationView = mActivity.findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(2).setChecked(true);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new mAdapter(mActivity,
                R.layout.ingredient_button, ingredients);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mEditText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if (keyCode == KeyEvent.KEYCODE_ENTER)
                    {
                        ingredients.add(0, mEditText.getText().toString());
                        mAdapter.notifyItemInserted(0);
                        mEditText.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        mFindButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Animation blindAnimation;
                blindAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.myblink);
                blindAnimation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        RequestUrlCreator creator = new RequestUrlCreator(ingredients);
                        req = new HttpGetRequestTask(
                                (basicUrl = creator.makeRequestString()),
                                mProgressView,
                                (ViewGroup) mActivity.findViewById(R.id.ingredients_relative));

                        req.delegate = IngredientsInputFragment.this;
                        req.execute();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                });

                mFindButton.startAnimation(blindAnimation);

            }
        });
    }

    @Override
    public void onResponse(String res)
    {
        reqBody = res;
        //Log.e("REQBODY", reqBody);

        //MealsListFragment fragment = (MealsListFragment) mFragmentManager
        //        .findFragmentByTag(TAG_1);
        //Fragment fragment = null;
        fragment = new MealsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("reqBody", reqBody);
        bundle.putString("basicUrl", basicUrl);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.addToBackStack(null).commit();

    }

    class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder>
    {
        private LayoutInflater inflater;
        private int layout;
        private ArrayList<String> mDataSet;

        mAdapter(Context context, int resource, ArrayList<String> list)
        {
            mDataSet = list;
            layout = resource;
            inflater = LayoutInflater.from(context);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            final Button button;
            final TextView name;

            ViewHolder(View view)
            {
                super(view);
                button = view.findViewById(R.id.ingredient_button);
                name = view.findViewById(R.id.ingredient_name);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = inflater.inflate(layout, parent, false);
            return new ViewHolder(view);
        }

        /*private Context context;
        private View animatedView;
        public mAdapter(Context context){

            this.context = context;
            this.animatedView = mFindButton;
        }*/

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            holder.name.setText(mDataSet.get(position));
            holder.button.setOnClickListener(new View.OnClickListener()
            {


                @Override
                public void onClick(View v)
                {
                    Animation animp;
                    animp = AnimationUtils.loadAnimation(mActivity, R.anim.myblink);
                    animp.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            simpleCallback.onSwiped(holder, ItemTouchHelper.LEFT);
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                    });
                    holder.button.startAnimation(animp);
                }
            });
        }

        public int getItemCount()
        {
            return mDataSet.size();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        outState.putStringArrayList(INGREDIENTS_KEY_, ingredients);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ingredients.addAll(0, savedInstanceState.getStringArrayList(INGREDIENTS_KEY_));
        }
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled)
    {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup)
            {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }

}
