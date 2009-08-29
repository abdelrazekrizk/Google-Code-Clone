class CreateNewsPosts < ActiveRecord::Migration
  def self.up
    create_table :news_posts do |t|
      t.string :title
      t.string :image
      t.text :contents
      t.references :user

      t.timestamps
    end
  end

  def self.down
    drop_table :news_posts
  end
end
