class CreateCharacters < ActiveRecord::Migration
  def self.up
    create_table :characters do |t|
      t.string :name
      t.integer :character_class_id
      t.integer :character_race_id
      t.float :ep, :default => 0, :null => false
      t.float :gp, :default => 0, :null => false
      t.float :pr, :default => 0, :null => false
      t.boolean :is_active, :default => 1, :null => false
    end
  end

  def self.down
    drop_table :characters
  end
end
