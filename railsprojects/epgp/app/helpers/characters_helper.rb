module CharactersHelper
  def char_link(c)
    link_to c.name, :controller => 'characters', :action => 'show', :id => c.name
  end
  
  # TODO: this nonsense is -nasty-, figure out how to do this beautifully.
  def characters_table(characters)
    str = "<table class='sortable' id='chartable' width='100%'><thead><tr>"
    ["Name", "Class", "Race", "EP", "GP", "PR"].each do |column|
      str += "<th>" + column + "</th>"
    end
    str += "</tr></thead>"
    str += "<tbody>"
    characters.each do |c|
      str += "<tr>"
      [char_link(c), c.character_class.name, c.character_race.name, c.ep, c.gp, c.pr].each do |v|
        str += "<td>" + v.to_s + "</td>"
      end
      str += "</tr>"
    end
    str += "</tbody></table>"
    return str
  end
  
  def admin_characters_table(characters)
    t = "<table class='sortable' id='chartable' width=\"100%\"><thead><tr>"
    ["Name", "Class", "Race", "Active?"].each do |c|
      t += "<th>" + c + "</th>"
    end
    t += "</tr></thead>"
    t += "<tbody>"
    characters.each do |c|
      t += "<tr>"
      [c.name, c.character_class.name, c.character_race.name, c.is_active].each do |v|
        t += "<td>" + v.to_s + "</td>"
      end
      t += "<td>"
      t += link_to "Edit", :action => "edit", :id => c.id
      #t += link_to "Toggle Status", :action => "toggle_activation", :id => c.id
      t += "</td></tr>"
    end
    t += "</tbody>"
    t += "</table>"
    t += link_to "New", :action => "new"
  end
end