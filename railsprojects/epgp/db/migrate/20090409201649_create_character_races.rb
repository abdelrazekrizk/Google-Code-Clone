class CreateCharacterRaces < ActiveRecord::Migration
  def self.up
    create_table :character_races do |t|
      t.string :name
    end
  end

  def self.down
    drop_table :character_races
  end
end
