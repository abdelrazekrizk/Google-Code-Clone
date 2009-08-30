class CreateForumForums < ActiveRecord::Migration
  def self.up
    create_table :forum_forums do |t|
      t.string :name
      t.text :description
      t.references :forum_category
      t.integer :priority, :default => 0

      t.timestamps
    end
  end

  def self.down
    drop_table :forum_forums
  end
end
