class CreateDecays < ActiveRecord::Migration
  def self.up
    create_table :decays do |t|
      t.datetime :performed_on
    end
  end

  def self.down
    drop_table :decays
  end
end
