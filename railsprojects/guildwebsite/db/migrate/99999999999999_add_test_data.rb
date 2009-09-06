class AddTestData < ActiveRecord::Migration
  def self.up
    user = User.new
    user.login = "Domia"
    user.password = "password"
    user.password_confirmation = "password"
    user.email = "asuah@gatech.edu"
    user.save
    
    (1..5).each do |i| 
      news = NewsPost.new
      news.user = user
      news.contents = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ut arcu in libero viverra suscipit. Cras et orci enim, in ultricies purus. Duis auctor, orci at dignissim euismod, metus sem pharetra diam, semper varius mauris ante sit amet nisl. Sed eleifend egestas sagittis. Aliquam nibh massa, convallis et volutpat a, gravida eu sapien. Ut dui justo, rutrum sed iaculis ac, egestas in enim. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis adipiscing tempor semper. Donec malesuada ipsum id odio egestas pellentesque. Maecenas a erat est. Curabitur vel nisl sed dui adipiscing placerat id laoreet leo."
      news.title = "Lorem Ipsum #{i}"
      news.created_at = Time.at(Time.now.to_i + i)
      news.save
    end
    
    p = UserPermissions.new
    p.user = user
    p.permission = :admin
    p.save
#    
    cat = ForumCategory.new
    cat.name = "General"
    cat.priority = 9;
    cat.save
    cat2 = ForumCategory.new
    cat2.name = "Off-topic"
    cat2.priority = 8;
    cat2.save
    cat3 = ForumCategory.new
    cat3.name = "Empty category"
    cat3.priority = 7
    cat3.save
    
    
    (1..5).each do |i|
      forum = ForumForum.new
      forum.name = "Example Forum #{i} in #{cat.name}"
      forum.forum_category_id = cat.id
      forum.description = "Description here"
      forum.priority = 10 - i;
      forum.save!
    end
#    
    (1..5).each do |i|
      top = ForumTopic.new
      top.title = "Example Topic #{i}"
      top.user = user
      top.forum_forum_id = ForumForum.find(1).id
      top.updated_at = Time.at(Time.now.to_i + i)
      top.save
    end
#    
    (1..5).each do |i|
      post = ForumPost.new
      post.user = user
      post.title = "Re: Example Topic 1 in General"
      post.content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ut arcu in libero viverra suscipit. Cras et orci enim, in ultricies purus. Duis auctor, orci at dignissim euismod, metus sem pharetra diam, semper varius mauris ante sit amet nisl. Sed eleifend egestas sagittis. Aliquam nibh massa, convallis et volutpat a, gravida eu sapien. Ut dui justo, rutrum sed iaculis ac, egestas in enim. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis adipiscing tempor semper. Donec malesuada ipsum id odio egestas pellentesque. Maecenas a erat est. Curabitur vel nisl sed dui adipiscing placerat id laoreet leo."
      post.forum_topic_id = ForumTopic.find(1).id
      post.created_at = Time.at(Time.now.to_i + i)
      post.save
    end
  end

  def self.down
  end
end
