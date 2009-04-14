module BossesHelper
  def boss_link(boss)
    link_to boss.name, :controller => 'bosses', :action => 'show', :id => boss.id
  end
  
  def boss_zone_link(boss)
    link_to boss.zone.short_name + " - " + boss.name, :controller => 'bosses', :action => 'show', :id => boss.id
  end
  
  def admin_bosses_table(bosses)
    table = "<table><thead><tr>"
    ["Name", "Zone"].each do |column|
      table += "<th>" + column + "</th>"
    end
    table += "</tr></thead>"
    table += "<tbody>"
    bosses.each do |x|
      table += "<tr>"
      [x.name, x.zone.name].each do |v|
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
  
  def boss_combo_box(name, default)
    t = "<td><select name=\"#{name}\">"
    Boss.find(:all, :order => "name ASC").each do |x|
      id, choice = x.id, x.name
      if(choice == default)
        t += "<option value=\"#{id}\" selected=\"selected\">#{choice}</option>"
      else
        t += "<option value=\"#{id}\">#{choice}</option>"
      end
    end
    t += "</select></td>"
  end
end
