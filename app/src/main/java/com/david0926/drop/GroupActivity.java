package com.david0926.drop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.david0926.drop.adapter.GroupAdapter;
import com.david0926.drop.databinding.ActivityGroupBinding;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;

public class GroupActivity extends AppCompatActivity {

    private ObservableArrayList<GroupModel> groupItems = new ObservableArrayList<>();

    private ActivityGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);

        //finish activity, when back button pressed
        binding.toolbarGroup.setNavigationOnClickListener(view -> finish());

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerGroup.setLayoutManager(wrapper);

        GroupAdapter adapter = new GroupAdapter();
        binding.recyclerGroup.setAdapter(adapter);
        binding.setGroupList(groupItems);

        adapter.setOnItemClickListener((view, item) -> {
//            Intent intent = new Intent(this, ArticleActivity.class);
//            intent.putExtra("article", item);
//            startActivity(intent);
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        GroupModel model = new GroupModel();
        model.setName("선린인터넷고등학교 분실물센터");
        model.setPhoto(getString(R.string.test_image));

        GroupModel model2 = new GroupModel();
        model2.setName("디지털미디어고등학교 분실물센터");
        model2.setPhoto(getString(R.string.test_image));

        GroupModel model3 = new GroupModel();
        model3.setName("분실한 정신줄 관리센터");
        model3.setPhoto(getString(R.string.test_image));

        groupItems.add(model);
        groupItems.add(model2);
        groupItems.add(model3);

        binding.btnGroupNew.setOnClickListener(view ->
                startActivity(new Intent(GroupActivity.this, GroupNewActivity.class)));

    }
}
