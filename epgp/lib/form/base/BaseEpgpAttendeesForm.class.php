<?php

/**
 * EpgpAttendees form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseEpgpAttendeesForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'raidid'   => new sfWidgetFormPropelChoice(array('model' => 'EpgpRaids', 'add_empty' => true)),
      'playerid' => new sfWidgetFormPropelChoice(array('model' => 'EpgpRoster', 'add_empty' => true)),
      'id'       => new sfWidgetFormInputHidden(),
    ));

    $this->setValidators(array(
      'raidid'   => new sfValidatorPropelChoice(array('model' => 'EpgpRaids', 'column' => 'id', 'required' => false)),
      'playerid' => new sfValidatorPropelChoice(array('model' => 'EpgpRoster', 'column' => 'id', 'required' => false)),
      'id'       => new sfValidatorPropelChoice(array('model' => 'EpgpAttendees', 'column' => 'id', 'required' => false)),
    ));

    $this->widgetSchema->setNameFormat('epgp_attendees[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'EpgpAttendees';
  }


}
