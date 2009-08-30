class CreateForumCategories < ActiveRecord::Migration
  def self.up
    create_table :forum_categories do |t|
      t.string :name
      t.text :description
      t.integer :parent

      t.timestamps
    end
  end

  def self.down
    drop_table :forum_categories
  end
end
