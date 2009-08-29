class NewsTestData < ActiveRecord::Migration
  def self.up
    user = User.new
    user.login = "Domia"
    user.password = "pass"
    user.email = "asuah@gatech.edu"
    user.save
    i = 1
    5.times do 
      news = NewsPost.new
      news.user = User.find_by_login("Domia")
      news.contents = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque ut arcu in libero viverra suscipit. Cras et orci enim, in ultricies purus. Duis auctor, orci at dignissim euismod, metus sem pharetra diam, semper varius mauris ante sit amet nisl. Sed eleifend egestas sagittis. Aliquam nibh massa, convallis et volutpat a, gravida eu sapien. Ut dui justo, rutrum sed iaculis ac, egestas in enim. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis adipiscing tempor semper. Donec malesuada ipsum id odio egestas pellentesque. Maecenas a erat est. Curabitur vel nisl sed dui adipiscing placerat id laoreet leo."
      news.title = "Lorem Ipsum #{i}"
      news.created_at = Time.at(Time.now.to_i + i)
      news.save
      i += 1
    end
  end

  def self.down
    user.delete_all
    news.delete_all
  end
end
