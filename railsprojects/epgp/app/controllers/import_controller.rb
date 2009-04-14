class ImportController < ApplicationController
  def index
  end
  
  def parse_new_characters
    @data = EqdkpXmlParserController.parse(params[:xml][:data])
    @new_attendees = []
    @data[:attendees].each do |a|
      if(Character.find_by_name(a[:name]).nil?)
        @new_attendees += [a]
      end
    end
    @xml = params[:xml][:data]
    if(@new_attendees.length == 0)
      #redirect_to :action => :parse_raid
    end
  end
  
  def parse_raid
    @data = EqdkpXmlParserController.parse(params[:xml][:xml_data])
    if(!params[:attendees].nil?)
      params[:attendees].each do |k, a|
        if(a[:import] == "1")
          char = Character.create! :name => a[:name], :character_class_id => a[:charclass], :character_race_id => a[:charrace]
        end
      end
      flash[:notice] = "Characters imported successfully."
    end
    @attendees = ""
    @data[:attendees].each do |a|
      if(!Character.find_by_name(a[:name]).nil?)
        @attendees += a[:name] + "\r\n"
      end
    end
    
    @loot = @data[:loot]
  end
  
  def create_raid
    raidinfo = params[:new_raid]
    
    time = DateTime.new(raidinfo["datetime(1i)"].to_i, raidinfo["datetime(2i)"].to_i, raidinfo["datetime(3i)"].to_i, raidinfo["datetime(4i)"].to_i, raidinfo["datetime(5i)"].to_i, raidinfo["datetime(6i)"].to_i)
    
    raid = Raid.create! :note => raidinfo[:note], :base_value => raidinfo[:base_value].to_i, :time => time, :boss_id => raidinfo[:boss]
    
    params[:items].each do |k, v|
      item = Item.create! :name => v[:name], :wowitemid => v[:wowitemid], :raid_id => raid.id, :base_value => v[:value], :character_id => v[:awardee]
    end
    
    raid.attendees_as_str = params[:new_raid][:attendees]
    
    flash[:notice] = "Raid imported successfully!"
    redirect_to :controller => "raids", :action => "index"
  end
end
