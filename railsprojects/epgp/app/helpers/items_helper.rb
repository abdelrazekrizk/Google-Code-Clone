module ItemsHelper
  def item_link(item)
    link_to item.name, :controller => 'items', :action => 'show', :id => item.wowitemid
  end
  
  def items_table(items)
    str = "<table class='sortable' id='items'><thead><tr>"
    ["Date", "Name", "Base", "Decayed"].each do |column|
      str += "<th>" + column + "</th>"
    end
    str += "</tr></thead>"
    str += "<tbody>"
    items.each do |i|
      str += "<tr>"
      [raid_link(i.raid, i.raid.date), item_link(i), i.base_value, i.decayed_value].each do |v|
        str += "<td>" + v.to_s + "</td>"
      end
      str += "</tr>"
    end
    str += "</tbody></table>"
    return str
  end
  
  def awardee_table(items)
    str = "<table class='sortable' id='awardees'><thead><tr>"
    ["Date", "Awardee", "Raid Note", "Base", "Decayed"].each do |column|
      str += "<th>" + column + "</th>"
    end
    str += "</tr></thead>"
    str += "<tbody>"
    items.each do |i|
      str += "<tr>"
      [raid_link(i.raid, i.raid.date), char_link(i.character), raid_link(i.raid, i.raid.note), i.base_value, i.decayed_value].each do |v|
        str += "<td>" + v.to_s + "</td>"
      end
      str += "</tr>"
    end
    str += "</tbody></table>"
  end
  
  def admin_items_table(items)
    str = "<table class='sortable' id='adminitems'><thead><tr>"
    ["Date", "Name", "Awarded To", "Base", "Decayed"].each do |column|
      str += "<th>" + column + "</th>"
    end
    str += "</tr></thead>"
    str += "<tbody>"
    items.each do |i|
      str += "<tr>"
      [i.raid.date, i.name, i.character.name, i.base_value, i.decayed_value].each do |v|
        str += "<td>" + v.to_s + "</td>"
      end
      str += "<td>" + link_to("Edit", :action => :edit, :id => i.id) + "<br/>" + link_to("Destroy", :action => :destroy, :id => i.id) + "</td>"
      str += "</tr>"
    end
    str += "</tbody></table>"
    str += link_to "New", :action => :new
    return str
  end
end
