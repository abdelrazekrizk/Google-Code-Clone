class CreateRaids < ActiveRecord::Migration
  def self.up
    create_table :raids do |t|
      t.string :note
      t.integer :boss_id
      t.float :base_value
      t.float :inflated_value
      t.float :inflated_multiplier
      t.datetime :time
    end
  end

  def self.down
    drop_table :raids
  end
end
