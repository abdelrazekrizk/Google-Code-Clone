class CreateForumForums < ActiveRecord::Migration
  def self.up
    create_table :forum_forums do |t|
      t.string :name
      t.text :description
      t.integer :priority
      t.references :forum_category, :null => false

      t.timestamps
    end
  end

  def self.down
    drop_table :forum_forums
  end
end
