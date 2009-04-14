module RaidsHelper
  
  def raid_link(raid, text = raid.note)
    link_to text, :controller => 'raids', :action => 'show', :id => raid.id
  end 
  
  
  def raids_table(raids)
    table = "<table class='sortable' id='raids'><thead><tr>"
    ["Date", "Zone - Boss", "Note", "Base", "Decayed"].each do |column|
      table += "<th>" + column + "</th>"
    end
    table += "</tr></thead>"
    table += "<tbody>"
    raids.each do |x|
      table += "<tr>"
      [raid_link(x, x.date), boss_zone_link(x.boss), raid_link(x), x.base_value, x.decayed_value].each do |v|
        table += "<td>" + v.to_s + "</td>"
      end
      table += "</tr>"
    end
    table += "</tbody></table>"
    return table
  end
  
  def admin_raids_table(raids)
    table = "<table class='sortable' id='raids'><thead><tr>"
    ["Date", "Zone - Boss", "Note", "Base", "Decayed"].each do |column|
      table += "<th>" + column + "</th>"
    end
    table += "</tr></thead>"
    table += "<tbody>"
    raids.each do |x|
      table += "<tr>"
      [x.date, x.boss.name, x.note, x.base_value, x.decayed_value].each do |v|
        table += "<td>" + v.to_s + "</td>"
      end
      table += "<td>"
      table += link_to "Edit", :action => "edit", :id => x.id
      table += "<br />"
      table += link_to "Destroy", {:action => :destroy, :id => x.id}, :confirm => "Are you sure? Doing so will also destroy associated items."
      table += "</td></tr>"
    end
    table += "</tbody></table>"
    table += link_to "New", :action => "new"
    return table
  end
  
  def attendance_table(characters)
    table = "<table>"
    i = 0
    columns = characters.length / 4
    table += "<tr>"
    characters.each do |c|
      table += "<th>" + char_link(c) + "</th>"
      i += 1
      if(i - columns == 0) then
        i = 0
        table += "</tr><tr>"
      end
    end
    table += "</tr>"
    table += "</table>"
    return table
  end

end
