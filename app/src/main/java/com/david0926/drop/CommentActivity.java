package com.david0926.drop;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.drop.adapter.CommentAdapter;
import com.david0926.drop.databinding.ActivityCommentBinding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.CommentModel;
import com.david0926.drop.model.UserModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.UserCache;

public class CommentActivity extends AppCompatActivity {

    private ObservableArrayList<CommentModel> commentItems = new ObservableArrayList<>();

    private ArticleModel article;
    private ActivityCommentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment);
        binding.setContent("");

        //finish activity, when back button pressed
        binding.toolbarComment.setNavigationOnClickListener(view -> finish());

        binding.setIsImportant(getIntent().getBooleanExtra("is_important", false));

        article = (ArticleModel)getIntent().getSerializableExtra("article");
        binding.setType(article.getType());

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerComment.setLayoutManager(wrapper);

        CommentAdapter adapter = new CommentAdapter();
        binding.recyclerComment.setAdapter(adapter);
        binding.setCommentList(commentItems);

        //테스트값
        UserModel userModel = new UserModel();
        userModel.setName("정찬효");
        userModel.setPhoto(getString(R.string.test_image));

        CommentModel model = new CommentModel();
        model.setContent("테스트 댓글입니다");
        model.setTime("2020/06/30 16:00:00");
        model.setIsImportant(false);
        model.setUser(userModel);

        CommentModel model2 = new CommentModel();
        model2.setContent("테스트 댓글2 입니다");
        model2.setTime("2020/06/30 16:00:00");
        model2.setIsImportant(true);
        model2.setUser(userModel);

        commentItems.add(model);
        commentItems.add(model2);

        binding.btnComment.setOnClickListener(view -> {
            if(binding.getContent().isEmpty()) return;
            uploadComment(article.get_id(), binding.getContent(), binding.getIsImportant());
        });

        adapter.setOnItemClickListener((view, item) -> {

        });
        adapter.setOnItemLongClickListener((view, item) -> {
            if (!item.getUser().get_id().equals(UserCache.getUser(this).get_id())) return false;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("댓글 삭제하기")
                    .setMessage("이 댓글을 삭제할까요?")
                    .setPositiveButton("삭제", (dialogInterface, i) -> {
                        //여기에 댓글 삭제 구현
                    })
                    .setNegativeButton("취소", (dialogInterface, i) -> {});
            builder.show();

            return true;
        });

    }

    void uploadComment(String postId, String content, Boolean isImportant){
        //여기에 댓글 작성 구현
    }
}
