require 'test_helper'

class ForumForumsControllerTest < ActionController::TestCase
  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:forum_forums)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create forum_forum" do
    assert_difference('ForumForum.count') do
      post :create, :forum_forum => { }
    end

    assert_redirected_to forum_forum_path(assigns(:forum_forum))
  end

  test "should show forum_forum" do
    get :show, :id => forum_forums(:one).to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => forum_forums(:one).to_param
    assert_response :success
  end

  test "should update forum_forum" do
    put :update, :id => forum_forums(:one).to_param, :forum_forum => { }
    assert_redirected_to forum_forum_path(assigns(:forum_forum))
  end

  test "should destroy forum_forum" do
    assert_difference('ForumForum.count', -1) do
      delete :destroy, :id => forum_forums(:one).to_param
    end

    assert_redirected_to forum_forums_path
  end
end
