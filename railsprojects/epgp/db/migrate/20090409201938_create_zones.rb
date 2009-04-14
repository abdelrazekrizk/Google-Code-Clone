class CreateZones < ActiveRecord::Migration
  def self.up
    create_table :zones do |t|
      t.string :name
      t.string :short_name
    end
  end

  def self.down
    drop_table :zones
  end
end
