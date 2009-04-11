class CreateItems < ActiveRecord::Migration
  def self.up
    create_table :items do |t|
      t.string :name
      t.integer :character_id
      t.integer :raid_id
      t.integer :wowitemid
      t.float :base_value
      t.float :inflated_value
    end
  end

  def self.down
    drop_table :items
  end
end
