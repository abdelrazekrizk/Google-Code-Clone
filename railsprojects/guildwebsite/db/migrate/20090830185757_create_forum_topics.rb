class CreateForumTopics < ActiveRecord::Migration
  def self.up
    create_table :forum_topics do |t|
      t.references :user
      t.string :name
      t.references :forum_category

      t.timestamps
    end
  end

  def self.down
    drop_table :forum_topics
  end
end
