class RaidsController < ApplicationController
  before_filter :login_required, :except => %w[index show]
  
  def index
    @raids = Raid.find(:all, :order => "time DESC") #fixme: pagnate
  end
  
  def show
    @raid = Raid.find(params[:id])
  end
  
  def adminindex
    @raids = Raid.find(:all, :order => "time DESC") #fixme: pagnate
  end
  
  def edit
    @raid = Raid.find(params[:id])
  end
  
  def update
    r = Raid.find(params[:raid][:id])
    if(r.update_attributes(params[:raid]))
      flash[:notice] = "Updated successfully."
      redirect_to :action => 'adminindex'
    else
      flash[:error] = "Did not update successfully."
      redirect_to :action => 'edit'
    end
  end
  
  def create
    r = Raid.new(params[:raid])
    if(r.save)
      flash[:notice] = "Created successfully."
      redirect_to :action => 'adminindex'
    else
      flash[:error] = "Did not create successfully."
      redirect_to :action => 'new'
    end
  end
  
  def destroy
    if(Raid.find(params[:id]).destroy)
      flash[:notice] = "Raid destroyed successfully."
    else
      flash[:error] = "Could not destroy raid."
    end
    redirect_to :action => 'adminindex'
  end
end
