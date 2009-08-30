class CreateUserPermissions < ActiveRecord::Migration
  def self.up
    create_table :user_permissions do |t|
      t.references :user
      t.string :permission

      t.timestamps
    end
  end

  def self.down
    drop_table :user_permissions
  end
end
