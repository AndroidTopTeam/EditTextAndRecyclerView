package ru.mipt.feofanova.foodapp.fragments;

import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.mipt.feofanova.foodapp.DBHelper;
import ru.mipt.feofanova.foodapp.DBHelperRemoveAtTask;
import ru.mipt.feofanova.foodapp.GsonMealObject;
import ru.mipt.feofanova.foodapp.ImageDownloaderTask;
import ru.mipt.feofanova.foodapp.R;
import ru.mipt.feofanova.foodapp.Singleton;

import static ru.mipt.feofanova.foodapp.NavigationActivity.fragment;
import static ru.mipt.feofanova.foodapp.NavigationActivity.mFragmentManager;

public class FavoriteFragment extends Fragment implements ImageDownloaderTask.IImageResponseListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecipesAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ItemTouchHelper.SimpleCallback simpleCallback;
    private final ArrayList<String> mDataSet = new ArrayList<>();
    private final ArrayList<String> mUrlsSet = new ArrayList<>();
    private List<GsonMealObject> parsedJson;
    private ImageDownloaderTask imgResponseTask;
    private DBHelper mDBHelper;
    AppCompatActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_favorite, container,
                false);
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
                final String deletedItem = mDataSet.get(position);
                final String deletedUrl = mUrlsSet.get(position);
                Singleton.setParsedJsonResp(parsedJson);
                final GsonMealObject currentMeal = Singleton.getInstance().getParsedJsonResp().get(position);

                Snackbar snackbar = Snackbar
                        .make(mActivity.findViewById(R.id.favorite_relative), deletedItem + " deleted!", Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        parsedJson.add(currentMeal);
                        mDataSet.add(deletedItem);
                        mUrlsSet.add(deletedUrl);
                        mRecipesAdapter.notifyItemInserted(mDataSet.size() - 1);

                        mDBHelper.addValue(currentMeal.getTitle(), currentMeal.getIngredients(), currentMeal.getHref(),currentMeal.getThumbnail());
                    }
                });

                DBHelperRemoveAtTask remover = new DBHelperRemoveAtTask(mActivity, deletedItem);
                remover.execute();

                parsedJson.remove(position);
                mDataSet.remove(position);
                mUrlsSet.remove(position);
                mRecipesAdapter.notifyItemRemoved(position);

                snackbar.show();
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecyclerView = getActivity().findViewById(R.id.favorite_recipes_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecipesAdapter = new mAdapter(mDataSet, mUrlsSet);
        mRecyclerView.setAdapter(mRecipesAdapter);

        NavigationView navigationView = mActivity.findViewById(R.id.navigation_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        parsedJson = new ArrayList<>();
        mDBHelper = new DBHelper(getActivity());
        int numFavourite = mDBHelper.getCount();

        /*while ((numFavourite--) > 0)
            mDBHelper.removeLast();*/
        ArrayList<String> favorites;

        /*for (int i = 0; i < numFavourite; ++i) {
            favorites = mDBHelper.getValues(i + 1 );
            if (favorites.size() > 0) {
                GsonMealObject temp = new GsonMealObject(favorites.get(0), favorites.get(2), favorites.get(1), favorites.get(3));
                parsedJson.add(temp);
                mDataSet.add(favorites.get(0));
                mUrlsSet.add(favorites.get(3));

            }}*/

        mDataSet.clear();
        mUrlsSet.clear();
        ArrayList<ArrayList<String>> allFavourites = mDBHelper.getAllValues();
        for (int i=0;i<allFavourites.size();++i)
        {
            if(allFavourites.get(i).size()>0)
            {
                GsonMealObject temp = new GsonMealObject(allFavourites.get(i).get(0), allFavourites.get(i).get(2), allFavourites.get(i).get(1), allFavourites.get(i).get(3));
                parsedJson.add(temp);
                mDataSet.add(allFavourites.get(i).get(0));
                mUrlsSet.add(allFavourites.get(i).get(3));
            }
        }
    }

    @Override
    public void onResponse(Bitmap img, ImageView currentImage) {
        currentImage.setImageBitmap(img);
    }

    public class mAdapter extends RecyclerView.Adapter<mAdapter.ViewHolder> {
        private ArrayList<String> mDataSet;
        private ArrayList<String> mUrlsSet;

        class ViewHolder extends RecyclerView.ViewHolder {
            CardView mCardView;
            ImageView mImageView;
            TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mCardView = view.findViewById(R.id.recipe_card_view);
                mImageView = view.findViewById(R.id.recipe_card_view_image);
                mTextView = view.findViewById(R.id.recipe_name);
            }
        }

        public mAdapter(ArrayList<String> inputDataDet, ArrayList<String> urls) {
            mDataSet = inputDataDet;
            mUrlsSet = urls;
        }

        @Override
        public mAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_card_view, parent, false);

            return new FavoriteFragment.mAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final mAdapter.ViewHolder holder, int position) {
            holder.mTextView.setText(mDataSet.get(position));
            String url = mUrlsSet.get(position);
            holder.mImageView.setImageResource(R.drawable.placeholder); //заглушка

            imgResponseTask = new ImageDownloaderTask(url, holder.mImageView); ///args
            imgResponseTask.delegate = FavoriteFragment.this;
            imgResponseTask.execute();


            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Singleton.setParsedJsonResp(parsedJson);
                    fragment = new MenuFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("currentMealIndex", holder.getAdapterPosition());
                    fragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_content, fragment);
                    fragmentTransaction.addToBackStack(null).commit();
                }
            });
        }

        public int getItemCount() {
            return mDataSet.size();
        }
    }
}
