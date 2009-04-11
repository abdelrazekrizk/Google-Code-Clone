class CreateBosses < ActiveRecord::Migration
  def self.up
    create_table :bosses do |t|
      t.string :name
      t.integer :zone_id
    end
  end

  def self.down
    drop_table :bosses
  end
end
