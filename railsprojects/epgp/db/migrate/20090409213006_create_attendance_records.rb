class CreateAttendanceRecords < ActiveRecord::Migration
  def self.up
    create_table :attendance_records do |t|
      t.integer :character_id
      t.integer :raid_id
    end
  end

  def self.down
    drop_table :attendance_records
  end
end
