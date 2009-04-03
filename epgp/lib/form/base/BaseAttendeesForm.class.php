<?php

/**
 * Attendees form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseAttendeesForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'raidid'   => new sfWidgetFormPropelChoice(array('model' => 'Raids', 'add_empty' => true)),
      'playerid' => new sfWidgetFormPropelChoice(array('model' => 'Roster', 'add_empty' => true)),
      'id'       => new sfWidgetFormInputHidden(),
    ));

    $this->setValidators(array(
      'raidid'   => new sfValidatorPropelChoice(array('model' => 'Raids', 'column' => 'id', 'required' => false)),
      'playerid' => new sfValidatorPropelChoice(array('model' => 'Roster', 'column' => 'id', 'required' => false)),
      'id'       => new sfValidatorPropelChoice(array('model' => 'Attendees', 'column' => 'id', 'required' => false)),
    ));

    $this->widgetSchema->setNameFormat('attendees[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'Attendees';
  }


}
